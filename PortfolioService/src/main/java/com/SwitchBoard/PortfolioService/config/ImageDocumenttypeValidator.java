
package com.SwitchBoard.PortfolioService.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageDocumenttypeValidator implements ConstraintValidator<ValidImageDocument, MultipartFile> {

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "application/pdf"
    );

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            // No file uploaded — that’s valid if it’s optional
            return true;
        }

        String contentType = file.getContentType();

        return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase());
    }
}
