package com.trace.traceproject.dto.request;

import lombok.Data;

@Data
public class ChangePasswordDto {
    String prevPassword;
    String changedPassword;
}
