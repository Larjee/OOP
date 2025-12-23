package ru.nsu.munkuev;

public class Main {
       public static void main(String[] args) {
           Table.Builder tableBuilder = new Table.Builder()
                   .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_LEFT)
                   .withRowLimit(8)
                   .addRow("Index", "Random");

           for (int i = 1; i <= 5; i ++) {
               final var value = (int) (Math.random() * 10);
               if (value > 5) {
                   tableBuilder.addRow(i, new Text.Bold(String.valueOf(value)));
               }
               else {
                   tableBuilder.addRow(i, (int) (Math.random() * 10));
               }
           }
           System.out.println(tableBuilder.build());


           Table.Builder t = new Table.Builder()
                   .withAlignments(Table.ALIGN_LEFT, Table.ALIGN_RIGHT, Table.ALIGN_CENTER)
                   .addRow("A", "B", "C")
                   .addRow(1, 2, 3);

           System.out.println(t.build());


       }
}
