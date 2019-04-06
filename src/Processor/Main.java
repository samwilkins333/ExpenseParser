package Processor;

import FileParser.FileParser;
import FileParser.FilterOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private interface Filter {
        boolean filter(Payment p);
    }

    private static class Payment {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        private Date date;
        private double amount;
        private String description;

        static Comparator<Payment> byAmount = Comparator.comparingDouble(p -> p.amount);
        static Comparator<Payment> byDate = Comparator.comparing(p -> p.date);

        Payment(String date, String amount, String description) {
            try {
                this.date = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.amount = Double.valueOf(amount);
            this.description = description;
        }

        Date date() {
            return date;
        }

        double amount() {
            return amount;
        }

        String description() {
            return description;
        }

        @Override
        public String toString() {
            int count = 10 - String.valueOf(amount).length();
            String buffer = new String(new char[count]).replace("\0", " ");
            String relevantDate = date.toString().split("00:00:00")[0];

            return (amount > 0 ? "DEPOSIT   " : "WITHDRAWAL") + "      $" +
                    amount + buffer + "      " + relevantDate + "      " + description;
        }
    }

    public static void main(String[] args) {
        List<Payment> deposits = new ArrayList<>();
        List<Payment> withdrawals = new ArrayList<>();

        try {
            FilterOptions options = new FilterOptions(false);
            options.addReplacement("[*\"]", "");
            options.addReplacement(",,,", ",");
            FileParser<Payment> parser = new FileParser<Payment>("Checking1.csv");
            String raw = parser.processFullText(options, false);
            Arrays.asList(raw.split("\n")).forEach(l -> {
                String[] info = l.split(",");
                Payment p = new Payment(info[0], info[1], info[2]);
                if (p.amount() > 0) {
                    deposits.add(p);
                } else {
                    withdrawals.add(p);
                }
            });

            parser.writeFile("./cleaned_payments.txt", sortJoin(deposits) + "\n\n" + sortJoin(withdrawals));

            filterSortPrint(deposits, p -> p.description.contains("BROWN"), Payment.byAmount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sortJoin(List<Payment> payments) {
        return payments.stream().sorted(Payment.byAmount)
                .map(Payment::toString).collect(Collectors.joining("\n"));
    }

    private static void filterSortPrint(List<Payment> payments, Filter filter, Comparator<Payment> c) {
        System.out.println();
        double total = payments.stream().filter(filter::filter).mapToDouble(d -> d.amount).sum();
        payments.stream().filter(filter::filter).sorted(c).forEach(System.out::println);
        System.out.printf("\nTotal: $%s\n\n", total);
    }
}

