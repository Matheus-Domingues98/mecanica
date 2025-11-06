package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.ProdutoDto;
import com.projetoweb.mecanica.entities.Produto;
import com.projetoweb.mecanica.repositories.ProdutoRepository;
import com.projetoweb.mecanica.mapper.ProdutoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<ProdutoDto> findAll() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(ProdutoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado com ID: " + id));
        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public ProdutoDto insert(ProdutoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ProdutoDto nao pode ser nulo");
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto nao pode ser vazio");
        }
        if (dto.getPreco() == null || dto.getPreco() < 0) {
            throw new IllegalArgumentException("Preco deve ser maior ou igual a zero");
        }

        Produto entity = new Produto();
        entity.setNomeProd(dto.getNome());
        entity.setPrecoProd(dto.getPreco());
        entity.setAtivo(true);

        entity = produtoRepository.save(entity);
        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public ProdutoDto update(Long id, ProdutoDto dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (dto == null) {
            throw new IllegalArgumentException("ProdutoDto nao pode ser nulo");
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto nao pode ser vazio");
        }
        if (dto.getPreco() == null || dto.getPreco() < 0) {
            throw new IllegalArgumentException("Preco deve ser maior ou igual a zero");
        }

        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado com ID: " + id));

        entity.setNomeProd(dto.getNome());
        entity.setPrecoProd(dto.getPreco());

        entity = produtoRepository.save(entity);
        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado com ID: " + id));
        produtoRepository.deleteById(id);
    }

    @Transactional
    public ProdutoDto desativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado com ID: " + id));

        entity.desativar();
        entity = produtoRepository.save(entity);

        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public ProdutoDto ativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado com ID: " + id));

        entity.ativar();
        entity = produtoRepository.save(entity);

        return ProdutoMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDto> findAllAtivos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .filter(Produto::isAtivo)
                .map(ProdutoMapper::toDto)
                .toList();
    }
}
