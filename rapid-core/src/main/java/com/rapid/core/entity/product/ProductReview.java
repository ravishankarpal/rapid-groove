package com.rapid.core.entity.product;

import com.rapid.core.dto.product.ReviewDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "product_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "user_name")
    private String name;
    private Integer rating;
    private String comment;
    private Date date;



    public ProductReview(ReviewDTO reviewDTO) {
        this.name = reviewDTO.getUserName();
        this.rating = reviewDTO.getRating();
        this.comment = reviewDTO.getComment();
        this.date = new Date();
    }
}
