package com.rapid.core.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo {

    private String sub;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String email;
    private boolean emailVerified;
    private String locale;
}
