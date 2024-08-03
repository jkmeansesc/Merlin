package org.haifan.merlin.model.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Options {
    @JsonProperty("num_keep")
    private Integer numKeep;
    private Integer mirostat;
    @JsonProperty("mirostat_eta")
    private Float mirostatEta;
    @JsonProperty("mirostat_tau")
    private Float mirostatTau;
    @JsonProperty("num_ctx")
    private Integer numCtx;
    @JsonProperty("num_batch")
    private Integer numBatch;
    @JsonProperty("num_gpu")
    private Integer numGpu;
    @JsonProperty("main_gpu")
    private Integer mainGpu;
    @JsonProperty("low_vram")
    private Boolean lowVRam;
    @JsonProperty("f16_kv")
    private Boolean f16KV;
    @JsonProperty("vocab_only")
    private Boolean vocabOnly;
    @JsonProperty("use_mmap")
    private Boolean useMmap;
    @JsonProperty("use_mlock")
    private Boolean useMlock;
    @JsonProperty("num_thread")
    private Integer numThread;
    @JsonProperty("repeat_last_n")
    private Integer repeatLastN;
    @JsonProperty("repeat_penalty")
    private Float repeatPenalty;
    @JsonProperty("presence_penalty")
    private Float presencePenalty;
    @JsonProperty("frequency_penalty")
    private Float frequencyPenalty;
    private Float temperature;
    private Integer seed;
    private List<String> stop;
    @JsonProperty("tfs_z")
    private Float tfsZ;
    @JsonProperty("num_predict")
    private Integer numPredict;
    @JsonProperty("top_k")
    private Integer topK;
    @JsonProperty("top_p")
    private Float topP;
    @JsonProperty("typical_p")
    private Float typicalP;
    @JsonProperty("penalize_newline")
    private Boolean penalizeNewline;
    private Boolean numa;
}
