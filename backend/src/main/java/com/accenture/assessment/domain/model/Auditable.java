package com.accenture.assessment.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass // inclui os campos em classes filhas, mas não gera uma tabela
@EntityListeners(AuditingEntityListener.class) // listener de auditoria do Spring.
@SQLDelete(sql = "UPDATE #{#entityName} SET excluido_em = NOW() WHERE id = ?") // sobrescreve o comando DELETE pelo softDelete										
public abstract class Auditable {
	@CreatedDate
	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@LastModifiedDate
	@Column(name = "atualizado_em", nullable = false)
	private LocalDateTime atualizadoEm;

	@SoftDelete(strategy = SoftDeleteType.DELETED) // remove os excluídos de todas as buscas
	@Column(name = "excluido_em", insertable=false, updatable=false)
	private LocalDateTime excluidoEm;

	// Getters and Setters
	public LocalDateTime getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(LocalDateTime criadoEm) {
		this.criadoEm = criadoEm;
	}

	public LocalDateTime getAtualizadoEm() {
		return atualizadoEm;
	}

	public void setAtualizadoEm(LocalDateTime atualizadoEm) {
		this.atualizadoEm = atualizadoEm;
	}

	public LocalDateTime getExcluidoEm() {
		return excluidoEm;
	}

	public void setExcluidoEm(LocalDateTime excluidoEm) {
		this.excluidoEm = excluidoEm;
	}
}
