/*
 * Leibniz Bioactives Cloud
 * Copyright 2017 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.ipb_halle.lbac.material.subtype.structure;

import java.util.Scanner;

/**
 *
 * @author fmauz
 */
public class V2000 implements MoleculeStructureModel {

    @Override
    public boolean isEmptyMolecule(String s) throws Exception{
        try (Scanner scanner = new Scanner(s)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                
                String line = scanner.nextLine();
                
                if (lineNumber == 3) {
                    String atoms = getNumberOfAtoms(line);
                    return Integer.parseInt(atoms) == 0;
                }
                lineNumber++;
                // process the line
            }
        }
        return false;
    }

    private String getNumberOfAtoms(String line) {
        String[] splittedLine = line.split(" ");
        for (String s : splittedLine) {
            if (!s.isEmpty()) {
                return s;
            }
        }
        return null;
    }
}
