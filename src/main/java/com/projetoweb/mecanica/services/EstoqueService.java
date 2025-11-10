package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.EstoqueDto;
import com.projetoweb.mecanica.entities.Estoque;
import com.projetoweb.mecanica.entities.Produto;
import com.projetoweb.mecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.mecanica.repositories.EstoqueRepository;
import com.projetoweb.mecanica.repositories.ProdutoRepository;
import com.projetoweb.mecanica.mapper.EstoqueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<EstoqueDto> findAll() {
        List<Estoque> estoques = estoqueRepository.findAll();
        return estoques.stream()
                .map(EstoqueMapper::toDto)
                .toList();
    }

    @Transactional
    public EstoqueDto insert(EstoqueDto dto) {
        // Bean Validation já garante que dto, produtoId e quantidade são válidos
        
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", dto.getProdutoId()));

        Estoque entity = new Estoque();
        entity.setProduto(produto);
        entity.setQuantidadeEstoque(dto.getQuantidade());

        entity = estoqueRepository.save(entity);
        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public EstoqueDto update(Long id, EstoqueDto dto) {
        // Bean Validation já garante que dto, produtoId e quantidade são válidos
        
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "ID", id));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", dto.getProdutoId()));

        entity.setProduto(produto);
        entity.setQuantidadeEstoque(dto.getQuantidade());
        entity = estoqueRepository.save(entity);

        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "ID", id));
        estoqueRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public EstoqueDto findById(Long id) {
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "ID", id));
        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public EstoqueDto venderProduto(Long id, Integer quantidade) {
        // Validação de quantidade deve ser feita no controller com @Positive
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "ID", id));

        // A validação de estoque suficiente é feita dentro do método venderProduto
        entity.venderProduto(quantidade);

        entity = estoqueRepository.save(entity);

        return EstoqueMapper.toDto(entity);
    }

    @Transactional
    public EstoqueDto adicionarProduto(Long id, Integer quantidade) {
        // Validação de quantidade deve ser feita no controller com @Positive
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "ID", id));

        entity.adicionarEstoque(quantidade);

        entity = estoqueRepository.save(entity);

        return EstoqueMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public boolean temEstoqueSuficiente(Long id, Integer quantidade) {
        // Validação de quantidade deve ser feita no controller com @PositiveOrZero
        Estoque entity = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "ID", id));

        return entity.temEstoqueSuficiente(quantidade);
    }
}
