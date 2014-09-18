package com.boaglio.casadocodigo.mongodb.dto;

import java.io.Serializable;
import java.util.List;

public class Filme implements Serializable {

	private static final long serialVersionUID = -5962850060839912887L;
	private Long id;
	private String titulo;
	private String ano;
	private Double nota;
	private Long votos;
	private List<String> categorias;
	private List<String> diretores;
	private List<Ator> atores;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public Long getVotos() {
		return votos;
	}

	public void setVotos(Long votos) {
		this.votos = votos;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}

	public List<String> getDiretores() {
		return diretores;
	}

	public void setDiretores(List<String> diretores) {
		this.diretores = diretores;
	}

	public List<Ator> getAtores() {
		return atores;
	}

	public void setAtores(List<Ator> atores) {
		this.atores = atores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		Filme other = (Filme) obj;
		if (id == null) {
			if (other.id != null) { return false; }
		} else if (!id.equals(other.id)) { return false; }
		return true;
	}

	@Override
	public String toString() {
		return "Filme [titulo=" + titulo + ", ano=" + ano + "]";
	}

}
