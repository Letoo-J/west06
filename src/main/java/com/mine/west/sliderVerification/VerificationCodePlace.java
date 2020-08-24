package com.mine.west.sliderVerification;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodePlace {
    private byte[] backName;
    private byte[] markName;
    private int xLocation;
    private int yLocation;
}
