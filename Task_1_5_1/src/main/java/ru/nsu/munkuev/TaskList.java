package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TaskList implements Block {
    public static final class Task {
        private final boolean done;
        private final Inline text;

        public Task(boolean done, Inline text) {
            this.done = done;
            this.text = Objects.requireNonNull(text);
        }

        @Override public boolean equals(Object o) {
            return (o instanceof Task t) && done == t.done && Objects.equals(text, t.text);
        }

        @Override public int hashCode() {
            return Objects.hash(done, text);
        }
    }

    private final List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = List.copyOf(tasks);
    }

    @Override public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            sb.append("- [").append(t.done ? "x" : " ").append("] ").append(t.text.toMarkdown());
            if (i + 1 < tasks.size()) sb.append("\n");
        }
        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        return (o instanceof TaskList tl) && Objects.equals(tasks, tl.tasks);
    }
    @Override public int hashCode() {
        return Objects.hash(tasks);
    }

    public static final class Builder {
        private final List<Task> tasks = new ArrayList<>();
        public Builder add(boolean done, Inline text) {
            tasks.add(new Task(done, text));
            return this;
        }
        public Builder add(boolean done, String text) {
            return add(done, new Text.Plain(text));
        }
        public TaskList build() {
            return new TaskList(tasks);
        }
    }
}
