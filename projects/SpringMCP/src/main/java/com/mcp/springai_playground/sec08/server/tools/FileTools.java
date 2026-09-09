package com.mcp.springai_playground.sec08.server.tools;

import com.mcp.springai_playground.sec08.server.dto.ToolResult;
import com.mcp.springai_playground.sec08.server.dto.VerificationCode;
import com.mcp.springai_playground.sec08.server.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.ai.mcp.annotation.context.McpSyncRequestContext;
import org.springframework.ai.mcp.annotation.context.StructuredElicitResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class FileTools {

    private static final Logger log = LoggerFactory.getLogger(FileTools.class);
    private final FileService fileService;

    public FileTools(FileService fileService) {
        this.fileService = fileService;
    }

    @McpTool(description = "List all the files")
    public List<String> listFiles() throws IOException {
        log.info("listing files");
        return fileService.listFiles();
    }

    @McpTool(description = "delete a file")
    public ToolResult deleteFile(McpSyncRequestContext context,
                                 @McpToolParam(description = "File name with extension (e.g., notes.txt)") String fileName) throws IOException {

        log.info("delete file {}", fileName);

        // Wait for user response
        // This call blocks the current thread. Use virtual threads for scalability.
        // spring.threads.virtual.enabled=true
        var result = requestVerificationCodeForDeletion(context, fileName);
        return switch (result.action()) {
            case ACCEPT -> deleteFile(fileName, result.structuredContent().code());
            case null, default -> new ToolResult("File deletion cancelled");
        };
    }

    private StructuredElicitResult<VerificationCode> requestVerificationCodeForDeletion(McpSyncRequestContext context, String fileName) {
        return context.elicit(spec -> spec.message("Enter Verfication code to delete %s".formatted(fileName))
                        .meta("progressToken", context.request().progressToken())
                , VerificationCode.class);
    }

    private ToolResult deleteFile(String fileName, String code) throws IOException {
        if("1234".equals(code)) {
            fileService.deleteFile(fileName);
            return new ToolResult("File deleted successfully");
        }
        return new ToolResult("Provided verification code is Not valid, File Not deleted");
    }
}
