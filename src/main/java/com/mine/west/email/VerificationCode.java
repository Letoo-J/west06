package com.mine.west.email;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {
    private Integer accountID;
    private Date effectiveTime;
}
