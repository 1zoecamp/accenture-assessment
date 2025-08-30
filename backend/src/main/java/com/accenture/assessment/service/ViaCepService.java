package com.accenture.assessment.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.accenture.assessment.dto.ViaCepResponse;

@Service
public class ViaCepService {

	private final WebClient webClient;

	public ViaCepService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("https://viacep.com.br/ws").build();
	}

	public Optional<ViaCepResponse> consultarCep(String cep) {
		try {
			ViaCepResponse response = this.webClient.get().uri("/{cep}/json", cep).retrieve()
					.bodyToMono(ViaCepResponse.class).block(); // .block() torna a chamada síncrona.

			// Se a resposta não for nula e não tiver o campo "erro", o CEP é válido
			if (response != null && !response.isErro()) {
				return Optional.of(response);
			}
		} catch (Exception e) {
			return Optional.empty();
		}
		return Optional.empty();
	}
}