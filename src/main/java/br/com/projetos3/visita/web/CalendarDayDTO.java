package br.com.projetos3.visita.web;

import java.time.LocalDate;

// DTO para transportar dados do dia do calendário para o Thymeleaf
public class CalendarDayDTO {
    private LocalDate date;
    private boolean isToday;
    private boolean isBaseDate; // O dia central/selecionado
    private boolean isClosed;   // Segunda-feira
    private boolean hasAviso;   // Tem aviso cadastrado

    // Getters e Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isToday() { return isToday; }
    public void setToday(boolean today) { isToday = today; }

    public boolean isBaseDate() { return isBaseDate; }
    public void setBaseDate(boolean baseDate) { isBaseDate = baseDate; }

    public boolean isClosed() { return isClosed; }
    public void setClosed(boolean closed) { isClosed = closed; }

    public boolean isHasAviso() { return hasAviso; }
    public void setHasAviso(boolean hasAviso) { this.hasAviso = hasAviso; }
}