package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.ServicoDto;
import com.projetoweb.mecanica.entities.Servico;
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
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico nao encontrado com ID: " + id));
        return ServicoMapper.toDto(entity);
    }

    @Transactional
    public ServicoDto insert(ServicoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("ServicoDto nao pode ser nulo");
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do servico nao pode ser vazio");
        }
        if (dto.getPreco() == null || dto.getPreco() < 0) {
            throw new IllegalArgumentException("Preco deve ser maior ou igual a zero");
        }
        if (dto.getDuracaoMinutos() == null || dto.getDuracaoMinutos() <= 0) {
            throw new IllegalArgumentException("Duracao deve ser maior que zero");
        }

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
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (dto == null) {
            throw new IllegalArgumentException("ServicoDto nao pode ser nulo");
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do servico nao pode ser vazio");
        }
        if (dto.getPreco() == null || dto.getPreco() < 0) {
            throw new IllegalArgumentException("Preco deve ser maior ou igual a zero");
        }
        if (dto.getDuracaoMinutos() == null || dto.getDuracaoMinutos() <= 0) {
            throw new IllegalArgumentException("Duracao deve ser maior que zero");
        }

        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico nao encontrado com ID: " + id));

        entity.setNomeServ(dto.getNome());
        entity.setDescricaoServ(dto.getDescricao());
        entity.setPrecoServ(dto.getPreco());
        entity.setDuracaoServ(Duration.ofMinutes(dto.getDuracaoMinutos()));

        entity = servicoRepository.save(entity);
        return ServicoMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico nao encontrado com ID: " + id));
        servicoRepository.deleteById(id);
    }

    @Transactional
    public ServicoDto desativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico nao encontrado com ID: " + id));

        entity.desativar();
        entity = servicoRepository.save(entity);

        return ServicoMapper.toDto(entity);
    }

    @Transactional
    public ServicoDto ativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servico nao encontrado com ID: " + id));

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
