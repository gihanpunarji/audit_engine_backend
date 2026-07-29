package com.gihan.AIAuditEngine.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gihan.AIAuditEngine.entity.*;
import com.gihan.AIAuditEngine.repository.AuditFindingRepo;
import com.gihan.AIAuditEngine.repository.AuditRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AiAuditProcessorService {

    private static final Logger log = LoggerFactory.getLogger(AiAuditProcessorService.class);

    private final ChatModel chatModel;
    private final S3Service s3Service;
    private final AuditRepo auditRepo;
    private final AuditFindingRepo auditFindingRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AiAuditProcessorService(ChatModel chatModel,
            S3Service s3Service,
            AuditRepo auditRepo,
            AuditFindingRepo auditFindingRepo) {
        this.chatModel = chatModel;
        this.s3Service = s3Service;
        this.auditRepo = auditRepo;
        this.auditFindingRepo = auditFindingRepo;
    }

    @Async
    public void processAuditAsync(UUID auditId) {
        log.info("Starting AI background audit processing for Audit ID: {}", auditId);

        Audit audit = auditRepo.findByIdWithTargetAndOrganization(auditId).orElse(null);
        if (audit == null) {
            log.error("Audit not found for processing ID: {}", auditId);
            return;
        }

        try {
            // Update status to PROCESSING
            audit.setStatus(AuditStatus.PROCESSING);
            auditRepo.save(audit);

            // Fetch document from S3
            InputStream fileStream = s3Service.downloadFile(audit.getS3Key());
            String mimeType = determineMimeType(audit.getOriginalFileName());

            // Build AI prompt
            String promptText = """
                    You are an enterprise document auditor AI engine.
                    Analyze the attached business document (%s document type: %s).

                    Perform 3 tasks:
                    1. Extract all structured business data (fields, dates, values, line items, metadata).
                    2. Audit the document for compliance, mathematical errors, suspicious values, policy violations, or anomalies.
                    3. Give a conclusion at the end wheather the uploaded document is legit or a fake one, like a scam.

                    Respond ONLY with a valid JSON object matching this exact schema:
                    {
                      "extractedData": { ... any key-value structured data extracted ... },
                      "findings": [
                        {
                          "category": "CATEGORY_NAME",
                          "title": "Short summary title",
                          "description": "Detailed explanation of what is wrong or notable",
                          "recommendation": "Suggested action",
                          "conclusion": "whether the uploaded document is legit or a fake one, like a scam",
                          "severity": "CRITICAL" | "HIGH" | "MEDIUM" | "LOW" | "INFO"
                        }
                      ]
                    }

                    Do not include markdown code block backticks if possible, just return raw valid JSON.
                    """
                    .formatted(audit.getAuditTarget().getName(), audit.getAuditTarget().getDocumentType());

            Media media = new Media(MimeTypeUtils.parseMimeType(mimeType), new InputStreamResource(fileStream));
            UserMessage userMessage = UserMessage.builder()
                    .text(promptText)
                    .media(media)
                    .build();

            log.info("Sending request to Gemini model for Audit ID: {}", auditId);
            String aiResponse = chatModel.call(new Prompt(userMessage)).getResult().getOutput().getText();
            log.info("Received AI response for Audit ID: {}", auditId);

            String cleanJson = aiResponse.replaceAll("```json", "").replaceAll("```", "").trim();

            JsonNode rootNode = objectMapper.readTree(cleanJson);
            JsonNode extractedDataNode = rootNode.get("extractedData");
            JsonNode findingsNode = rootNode.get("findings");

            // Save extracted JSON blob
            if (extractedDataNode != null) {
                audit.setRawExtractedData(extractedDataNode.toString());
            } else {
                audit.setRawExtractedData(cleanJson);
            }

            // Save individual findings
            if (findingsNode != null && findingsNode.isArray()) {
                for (JsonNode findingNode : findingsNode) {
                    AuditFinding finding = new AuditFinding();
                    finding.setAudit(audit);
                    finding.setCategory(findingNode.path("category").asText("GENERAL_ANOMALY"));
                    finding.setTitle(findingNode.path("title").asText("Anomaly Detected"));
                    finding.setDescription(findingNode.path("description").asText());
                    finding.setRecommendation(findingNode.path("recommendation").asText());

                    String severityStr = findingNode.path("severity").asText("MEDIUM").toUpperCase();
                    try {
                        finding.setSeverity(FindingSeverity.valueOf(severityStr));
                    } catch (IllegalArgumentException e) {
                        finding.setSeverity(FindingSeverity.MEDIUM);
                    }

                    auditFindingRepo.save(finding);
                }
            }

            audit.setStatus(AuditStatus.COMPLETED);
            audit.setProcessedAt(LocalDateTime.now());
            auditRepo.save(audit);
            log.info("Successfully completed AI Audit processing for Audit ID: {}", auditId);

        } catch (Exception e) {
            log.error("Failed to process Audit ID: {}", auditId, e);
            audit.setStatus(AuditStatus.FAILED);
            audit.setFailureReason(e.getMessage());
            audit.setProcessedAt(LocalDateTime.now());
            auditRepo.save(audit);
        }
    }

    private String determineMimeType(String filename) {
        if (filename == null)
            return "application/octet-stream";
        String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf"))
            return "application/pdf";
        if (lower.endsWith(".png"))
            return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg"))
            return "image/jpeg";
        if (lower.endsWith(".webp"))
            return "image/webp";
        return "application/octet-stream";
    }
}
