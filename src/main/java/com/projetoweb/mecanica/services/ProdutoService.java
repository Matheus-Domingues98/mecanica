package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.ProdutoDto;
import com.projetoweb.mecanica.entities.Produto;
import com.projetoweb.mecanica.exceptions.ResourceNotFoundException;
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
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", id));
        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public ProdutoDto insert(ProdutoDto dto) {
        // Bean Validation já garante que dto, nome e preço são válidos
        
        Produto entity = new Produto();
        entity.setNomeProd(dto.getNome());
        entity.setPrecoProd(dto.getPreco());
        entity.setAtivo(true);

        entity = produtoRepository.save(entity);
        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public ProdutoDto update(Long id, ProdutoDto dto) {
        // Bean Validation já garante que dto, nome e preço são válidos
        
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", id));

        entity.setNomeProd(dto.getNome());
        entity.setPrecoProd(dto.getPreco());

        entity = produtoRepository.save(entity);
        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", id));
        produtoRepository.deleteById(id);
    }

    @Transactional
    public ProdutoDto desativar(Long id) {
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", id));

        entity.desativar();
        entity = produtoRepository.save(entity);

        return ProdutoMapper.toDto(entity);
    }

    @Transactional
    public ProdutoDto ativar(Long id) {
        Produto entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", id));

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
