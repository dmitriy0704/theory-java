package dev.folomkin.core.collections_streamapi.sream_api;

import java.util.Comparator;import java.util.List;


    class Action {
        public static int sumCharCode(String str) {
            return str.codePoints().reduce(0, (v1, v2) -> v1 + v2);
        }
    }

    public class Code {
        public static void main(String[] args) {
            List<String> strings = List.of("Java PHP".split("\\s+"));
            String max = strings.stream()
                    .max(Comparator.comparingInt(Action::sumCharCode))
                    .orElse("empty");
            System.out.println(max);
        }
    }
