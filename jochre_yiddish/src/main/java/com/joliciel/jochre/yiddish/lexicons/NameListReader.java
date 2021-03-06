///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2012 Assaf Urieli
//
//This file is part of Jochre.
//
//Jochre is free software: you can redistribute it and/or modify
//it under the terms of the GNU Affero General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//Jochre is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Affero General Public License for more details.
//
//You should have received a copy of the GNU Affero General Public License
//along with Jochre.  If not, see <http://www.gnu.org/licenses/>.
//////////////////////////////////////////////////////////////////////////////
package com.joliciel.jochre.yiddish.lexicons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joliciel.jochre.lexicon.Lexicon;
import com.joliciel.jochre.lexicon.TextFileLexicon;

public class NameListReader {
  private static final Logger LOG = LoggerFactory.getLogger(NameListReader.class);

  private Set<NameLexicalEntry> allVariants = new TreeSet<>();
  private Set<String> entries = new TreeSet<>();

  private Writer variantWriter = null;
  private String defaultAttribute = null;

  private Set<String> exceptions = new HashSet<>(Arrays.asList(new String[] { "שיקען" }));

  public TextFileLexicon read(File file) throws IOException {
    Scanner scanner = new Scanner(file);
    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (!line.startsWith("#")) {
          String name = line;

          NameLexicalEntry entry = new NameLexicalEntry(name, "np", name, "s", defaultAttribute);
          allVariants.add(entry);
          entries.add(entry.text);

          String radical = YiddishTextUtils.removeEndForm(entry.text);
          String possessiveForm = radical;
          if (radical.endsWith("ס") || radical.endsWith("ש") || radical.endsWith("צ") || radical.endsWith("ת")) {
            possessiveForm += "עס";
          } else {
            possessiveForm += "ס";
          }
          if (!exceptions.contains(possessiveForm)) {
            NameLexicalEntry possessiveEntry = new NameLexicalEntry(possessiveForm, "np", name, "s",
                defaultAttribute + ",@poss");
            allVariants.add(possessiveEntry);
            entries.add(possessiveEntry.text);
          }

          String accusativeForm = radical;
          if (radical.endsWith("ל") || radical.endsWith("מ") || radical.endsWith("נ")) {
            accusativeForm += "ען";
          } else {
            accusativeForm += "ן";
          }
          if (!exceptions.contains(accusativeForm)) {
            NameLexicalEntry accusativeEntry = new NameLexicalEntry(accusativeForm, "np", name, "s",
                defaultAttribute + ",@acc,@dat");
            allVariants.add(accusativeEntry);
            entries.add(accusativeEntry.text);
          }
        }
      }

      if (this.variantWriter != null) {
        for (NameLexicalEntry variant : allVariants) {
          variantWriter.write(variant.toString() + "\n");
          variantWriter.flush();
        }
      }

      TextFileLexicon lexicon = new TextFileLexicon();
      for (String word : entries)
        lexicon.setEntry(word, 1);
      return lexicon;
    } finally {
      scanner.close();
    }
  }

  private static class NameLexicalEntry implements Comparable<NameLexicalEntry> {

    public NameLexicalEntry(String text, String category, String lemma, String morphology, String attributes) {
      super();
      this.text = text;
      this.category = category;
      this.lemma = lemma;
      this.morphology = morphology;
      this.attributes = attributes;
    }

    public String text = "";
    public String category = "";
    public String lemma = "";
    public String morphology = "";
    public String attributes = "";
    public String pronunciation = "";

    @Override
    public String toString() {
      return text + "\t" + category + "\t" + lemma + "\t" + morphology + "\t" + attributes + "\t" + pronunciation;
    }

    @Override
    public int compareTo(NameLexicalEntry o) {
      if (!this.text.equals(o.text))
        return this.text.compareTo(o.text);
      if (!this.category.equals(o.category))
        return this.category.compareTo(o.category);
      if (!this.lemma.equals(o.lemma))
        return this.lemma.compareTo(o.lemma);
      if (!this.morphology.equals(o.morphology))
        return this.morphology.compareTo(o.morphology);
      return this.attributes.compareTo(o.attributes);
    }

  }

  public Writer getVariantWriter() {
    return variantWriter;
  }

  public void setVariantWriter(Writer variantWriter) {
    this.variantWriter = variantWriter;
  }

  public static void main(String[] args) throws Exception {
    long startTime = (new Date()).getTime();
    try {
      String command = args[0];
      if (command.equals("load")) {
        NameListReader reader = new NameListReader();
        File file = new File(args[1]);
        Writer variantWriter = null;
        if (args.length > 2) {
          File variantFile = new File(args[2]);
          variantFile.delete();
          variantWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(variantFile, true), "UTF8"));

        }
        reader.setVariantWriter(variantWriter);

        reader.defaultAttribute = args[4];

        try {
          TextFileLexicon lexicon = reader.read(file);
          if (args.length > 3) {
            File lexiconFile = new File(args[3]);
            lexicon.serialize(lexiconFile);
          }
        } finally {
          if (variantWriter != null)
            variantWriter.close();
        }

      } else if (command.equals("deserialise")) {
        File memoryBaseFile = new File(args[1]);
        Lexicon lexicon = TextFileLexicon.deserialize(memoryBaseFile);
        String[] words = new String[] { "חײמס", "חױמס" };
        for (String word : words)
          LOG.debug("Have entry " + word + ": " + lexicon.getFrequency(word));
      } else {
        throw new RuntimeException("Unknown command: " + command);
      }

    } finally {
      long endTime = (new Date()).getTime() - startTime;
      LOG.debug("Total runtime: " + ((double) endTime / 1000) + " seconds");
    }
  }
}
