package com.wy.test.core.entity.userinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity {

	private String country;

	private String region;

	private String locality;

	private String streetAddress;

	private String addressFormatted;

	private String email;

	private String phoneNumber;

	private String postalCode;

	private String fax;
}