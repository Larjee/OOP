package ru.nsu.munkuev.pizzeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java -jar pizzeria-simulator.jar <config.json>");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        PizzeriaConfig config = mapper.readValue(new File(args[0]), PizzeriaConfig.class);
        new Pizzeria(config).open();
    }
}
