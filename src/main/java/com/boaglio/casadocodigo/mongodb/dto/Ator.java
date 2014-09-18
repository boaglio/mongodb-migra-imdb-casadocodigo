package com.boaglio.casadocodigo.mongodb.dto;

import java.io.Serializable;

public class Ator implements Serializable {

	private static final long serialVersionUID = -1904648580615812162L;

	private String nome;

	private String sexo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (nome == null ? 0 : nome.hashCode());
		result = prime * result + (sexo == null ? 0 : sexo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		Ator other = (Ator) obj;
		if (nome == null) {
			if (other.nome != null) { return false; }
		} else if (!nome.equals(other.nome)) { return false; }
		if (sexo == null) {
			if (other.sexo != null) { return false; }
		} else if (!sexo.equals(other.sexo)) { return false; }
		return true;
	}

	@Override
	public String toString() {
		return "Ator [nome=" + nome + ", sexo=" + sexo + "]";
	}

}
