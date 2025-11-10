package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.ServicoDto;
import com.projetoweb.mecanica.entities.Servico;
import com.projetoweb.mecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.mecanica.repositories.ServicoRepository;
import com.projetoweb.mecanica.mapper.ServicoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional(readOnly = true)
    public List<ServicoDto> findAll() {
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream()
                .map(ServicoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServicoDto findById(Long id) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servico", "ID", id));
        return ServicoMapper.toDto(entity);
    }

    @Transactional
    public ServicoDto insert(ServicoDto dto) {
        // Bean Validation já garante que dto, nome, preço e duração são válidos
        
        Servico entity = new Servico();
        entity.setNomeServ(dto.getNome());
        entity.setDescricaoServ(dto.getDescricao());
        entity.setPrecoServ(dto.getPreco());
        entity.setDuracaoServ(Duration.ofMinutes(dto.getDuracaoMinutos()));
        entity.setAtivo(true);

        entity = servicoRepository.save(entity);
        return ServicoMapper.toDto(entity);
    }

    @Transactional
    public ServicoDto update(Long id, ServicoDto dto) {
        // Bean Validation já garante que dto, nome, preço e duração são válidos
        
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servico", "ID", id));

        entity.setNomeServ(dto.getNome());
        entity.setDescricaoServ(dto.getDescricao());
        entity.setPrecoServ(dto.getPreco());
        entity.setDuracaoServ(Duration.ofMinutes(dto.getDuracaoMinutos()));

        entity = servicoRepository.save(entity);
        return ServicoMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servico", "ID", id));
        servicoRepository.deleteById(id);
    }

    @Transactional
    public ServicoDto desativar(Long id) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servico", "ID", id));

        entity.desativar();
        entity = servicoRepository.save(entity);

        return ServicoMapper.toDto(entity);
    }

    @Transactional
    public ServicoDto ativar(Long id) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servico", "ID", id));

        entity.ativar();
        entity = servicoRepository.save(entity);

        return ServicoMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ServicoDto> findAllAtivos() {
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream()
                .filter(Servico::isAtivo)
                .map(ServicoMapper::toDto)
                .toList();
    }
}
