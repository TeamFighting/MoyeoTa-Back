package com.moyeota.moyeotaproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

@Configuration
public class IamPortConfig {

	String apiKey = "0572581675081853";
	String secretKey = "6y7Ao8ko9s11PFbHTpTstDI6rdi0RIgBqz4RsU4aYtqam2R8uw7wIBuSslRoNzJ6OWArItEU1mShOm7G";

	@Bean
	public IamportClient iamportClient() {
		return new IamportClient(apiKey, secretKey);
	}
}
