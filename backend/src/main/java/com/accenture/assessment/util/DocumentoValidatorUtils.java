package com.accenture.assessment.util;

public final class DocumentoValidatorUtils {

	// Constructor
	private DocumentoValidatorUtils() {
	}

	private static final int[] WEIGHT_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] WEIGHT_CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

	public static boolean isValid(String documento) {
		if (documento == null) {
			return false;
		}
		String docLimpo = documento.replaceAll("[^0-9]", "");
		if (docLimpo.length() == 11) {
			return isCpfValido(docLimpo);
		} else if (docLimpo.length() == 14) {
			return isCnpjValido(docLimpo);
		}
		return false;
	}

	private static boolean isCpfValido(String cpf) {
		// Verifica se todos os dígitos são iguais
		if (cpf.matches("(\\d)\\1{10}"))
			return false;

		int d1 = calculateDigit(cpf.substring(0, 9), WEIGHT_CPF);
		int d2 = calculateDigit(cpf.substring(0, 9) + d1, WEIGHT_CPF);
		return cpf.equals(cpf.substring(0, 9) + d1 + d2);
	}

	private static boolean isCnpjValido(String cnpj) {
		// Verifica se todos os dígitos são iguais
		if (cnpj.matches("(\\d)\\1{13}"))
			return false;

		int d1 = calculateDigit(cnpj.substring(0, 12), WEIGHT_CNPJ);
		int d2 = calculateDigit(cnpj.substring(0, 12) + d1, WEIGHT_CNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + d1 + d2);
	}

	private static int calculateDigit(String str, int[] weight) {
		int sum = 0;
		for (int i = str.length() - 1, digit; i >= 0; i--) {
			digit = Integer.parseInt(str.substring(i, i + 1));
			sum += digit * weight[weight.length - str.length() + i];
		}
		sum = 11 - sum % 11;
		return sum > 9 ? 0 : sum;
	}
}
