package com.dcspa.prism.codegen;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private static final int DEFAULT_PAD = 6;

    private final CodeSequenceRepository codeSequenceRepository;

    @Transactional
    public String nextCode(String rawPrefix) {
        String prefix = normalizePrefix(rawPrefix);

        CodeSequence seq = codeSequenceRepository.lockByPrefix(prefix)
                .orElseGet(() -> {
                    CodeSequence created = new CodeSequence();
                    created.setPrefix(prefix);
                    created.setNextValue(1L);
                    return codeSequenceRepository.save(created);
                });

        long value = seq.getNextValue();
        seq.setNextValue(value + 1);
        codeSequenceRepository.save(seq);

        return prefix + leftPad(value, DEFAULT_PAD);
    }

    private static String normalizePrefix(String p) {
        if (p == null) {
            throw new IllegalArgumentException("Prefix null");
        }
        String s = p.trim().toUpperCase(Locale.ROOT);
        if (s.length() < 3) {
            throw new IllegalArgumentException("Prefix doit avoir au moins 3 caractères: " + p);
        }
        return s.substring(0, 3);
    }

    private static String leftPad(long v, int width) {
        String s = Long.toString(v);
        if (s.length() >= width) return s;
        return "0".repeat(width - s.length()) + s;
    }
}

