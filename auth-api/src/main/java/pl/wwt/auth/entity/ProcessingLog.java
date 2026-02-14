package pl.wwt.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "processing_log")
public class ProcessingLog {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private String inputText;
    private String outputText;
    private Instant createdAt = Instant.now();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ProcessingLog() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProcessingLog that = (ProcessingLog) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(inputText, that.inputText) && Objects.equals(outputText, that.outputText) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, inputText, outputText, createdAt);
    }

    @Override
    public String toString() {
        return "ProcessingLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", inputText='" + inputText + '\'' +
                ", outputText='" + outputText + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
