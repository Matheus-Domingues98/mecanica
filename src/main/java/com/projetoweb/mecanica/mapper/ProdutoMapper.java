package com.projetoweb.mecanica.mapper;

import com.projetoweb.mecanica.dto.ProdutoDto;
import com.projetoweb.mecanica.entities.Produto;

public class ProdutoMapper {

    public static ProdutoDto toDto(Produto produto) {
        if (produto == null) {
            return null;
        }
        return new ProdutoDto(
                produto.getIdProd(),
                produto.getNomeProd(),
                produto.getPrecoProd(),
                produto.isAtivo()
        );
    }

    public static Produto toEntity(ProdutoDto dto) {
        if (dto == null) {
            return null;
        }
        Produto produto = new Produto();
        produto.setIdProd(dto.getId());
        produto.setNomeProd(dto.getNome());
        produto.setPrecoProd(dto.getPreco());
        produto.setAtivo(dto.isAtivo());
        return produto;
    }
}
