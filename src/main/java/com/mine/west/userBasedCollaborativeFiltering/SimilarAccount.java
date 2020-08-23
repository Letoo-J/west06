package com.mine.west.userBasedCollaborativeFiltering;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SimilarAccount {
    private Integer accountID;
    private float similar;
}
