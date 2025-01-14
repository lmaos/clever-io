package org.clever.core.lang;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrefixMatchMap<Value> {
    private EntryNode<Value> header = new EntryNode<>();
    private int length;
    public Value put(String key, Value value) {
        EntryNode<Value> node = header;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            node = node.createNext(c);
        }
        Value oldValue = node.value;
        node.end = true;
        node.value = value;
        if (oldValue == null) {
            length++;
        }
        return oldValue;
    }

    public Value get(String text) {
        // 最长前缀匹配
        EntryNode<Value> node = header;
        EntryNode<Value> result = null;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            EntryNode<Value> next = node.next(c);
            if (next == null) {
                break;
            }
            if (next.end) {
                result = next;
            }
            node = next;
        }

        return result == null ? null : result.value;
    }


    public int size() {
        return length;
    }


    private static class EntryNode<Value> {
        private char word;
        private Map<Character, EntryNode<Value>> next = new ConcurrentHashMap<>();
        private Value value;
        private boolean end;

        public EntryNode<Value> createNext(char c) {
            if (next.containsKey(c)) {
                return next.get(c);
            }
            EntryNode<Value> node = new EntryNode<>();
            node.word = c;
            next.put(c, node);
            return node;
        }

        public EntryNode<Value> next(char c) {
            return next.get(c);
        }
    }
}
