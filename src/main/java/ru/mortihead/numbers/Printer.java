package ru.mortihead.numbers;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

public class Printer {
    static public void main(String args[]) throws Exception {
        PrintService pss[] = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < pss.length; ++i) {
            System.out.println(pss[i]);
            PrintService ps = pss[i];

            PrintServiceAttributeSet psas = ps.getAttributes();
            Attribute attributes[] = psas.toArray();
            for (int j = 0; j < attributes.length; ++j) {
                Attribute attribute = attributes[j];
                System.out.println("  attribute: " + attribute.getName());
                if (attribute instanceof PrinterName) {
                    System.out.println("    printer name: " + ((PrinterName) attribute).getValue());
                }
            }
            DocFlavor supportedFlavors[] = ps.getSupportedDocFlavors();
            for (int j = 0; j < supportedFlavors.length; ++j) {
                System.out.println("  flavor: " + supportedFlavors[j]);
            }
        }
    }
}