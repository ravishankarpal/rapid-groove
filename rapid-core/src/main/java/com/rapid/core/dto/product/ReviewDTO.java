package com.rapid.core.dto.product;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private String userName;
    private Integer rating;
    private String comment;
}
