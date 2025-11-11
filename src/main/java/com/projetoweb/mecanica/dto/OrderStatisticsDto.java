package com.projetoweb.mecanica.dto;

public class OrderStatisticsDto {

    private Long totalOrdens;
    private Long ordensFinalizadas;
    private Long ordensEmAndamento;
    private Long ordensCanceladas;
    private Double tempoMedioExecucaoMinutos;
    private Double tempoMedioTotalMinutos;

    public OrderStatisticsDto() {
    }

    public OrderStatisticsDto(Long totalOrdens, Long ordensFinalizadas, Long ordensEmAndamento, Long ordensCanceladas, Double tempoMedioExecucaoMinutos, Double tempoMedioTotalMinutos) {
        this.totalOrdens = totalOrdens;
        this.ordensFinalizadas = ordensFinalizadas;
        this.ordensEmAndamento = ordensEmAndamento;
        this.ordensCanceladas = ordensCanceladas;
        this.tempoMedioExecucaoMinutos = tempoMedioExecucaoMinutos;
        this.tempoMedioTotalMinutos = tempoMedioTotalMinutos;
    }

    public Long getTotalOrdens() {
        return totalOrdens;
    }

    public void setTotalOrdens(Long totalOrdens) {
        this.totalOrdens = totalOrdens;
    }

    public Long getOrdensFinalizadas() {
        return ordensFinalizadas;
    }

    public void setOrdensFinalizadas(Long ordensFinalizadas) {
        this.ordensFinalizadas = ordensFinalizadas;
    }

    public Long getOrdensEmAndamento() {
        return ordensEmAndamento;
    }

    public void setOrdensEmAndamento(Long ordensEmAndamento) {
        this.ordensEmAndamento = ordensEmAndamento;
    }

    public Long getOrdensCanceladas() {
        return ordensCanceladas;
    }

    public void setOrdensCanceladas(Long ordensCanceladas) {
        this.ordensCanceladas = ordensCanceladas;
    }

    public Double getTempoMedioExecucaoMinutos() {
        return tempoMedioExecucaoMinutos;
    }

    public void setTempoMedioExecucaoMinutos(Double tempoMedioExecucaoMinutos) {
        this.tempoMedioExecucaoMinutos = tempoMedioExecucaoMinutos;
    }

    public Double getTempoMedioTotalMinutos() {
        return tempoMedioTotalMinutos;
    }

    public void setTempoMedioTotalMinutos(Double tempoMedioTotalMinutos) {
        this.tempoMedioTotalMinutos = tempoMedioTotalMinutos;
    }
}
