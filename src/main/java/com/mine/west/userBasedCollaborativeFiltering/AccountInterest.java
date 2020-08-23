package com.mine.west.userBasedCollaborativeFiltering;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountInterest implements Comparable<AccountInterest> {
    private Integer blogID;
    private float interest;

    @Override
    public int compareTo(AccountInterest o) {
        return (int) (o.getInterest() - this.getInterest());
    }

    @Override
    public boolean equals(Object obj) {
        return this.getBlogID() == ((AccountInterest) obj).getBlogID();
    }
}
