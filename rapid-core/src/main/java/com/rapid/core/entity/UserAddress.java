package com.rapid.core.entity;


import com.rapid.core.dto.UserAddressDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id" )
    private Integer id;

    @Column(name = "name" )
    private String name;

    @Column(name = "phone_no" )

    private String phoneNo;

    @Column(name = "street_address" )

    private String streetAddress;

    @Column(name = "city" )

    private  String city;

    @Column(name = "pin_code" )

    private String pinCode;

    @Column(name = "state" )

    private  String state;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;


    public UserAddress(UserAddressDTO userAddressDTO){
        this.name = userAddressDTO.getName();
        this.phoneNo = userAddressDTO.getPhoneNo();
        this.streetAddress = userAddressDTO.getStreetAddress();
        this.city = userAddressDTO.getCity();
        this.pinCode = userAddressDTO.getPinCode();
        this.state = userAddressDTO.getState();
    }
}
