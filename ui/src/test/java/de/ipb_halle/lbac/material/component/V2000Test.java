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
package de.ipb_halle.lbac.material.component;

import de.ipb_halle.lbac.material.subtype.structure.V2000;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author fmauz
 */
public class V2000Test {

    @Test
    public void test01_isEmptyMolecule() throws Exception {
        V2000 instance = new V2000();
        String molecule = "n.n\n"
                + "\n"
                + "generated by MolPaintJS\n"
                + "  0  0  0  0  0  0            999\n"
                + "M  END";
        Assert.assertTrue(instance.isEmptyMolecule(molecule));

        molecule = "n.n\n"
                + "\n"
                + "generated by MolPaintJS\n"
                + " 2  1  0  0  0  0            999\n"
                + "                   -2.9091    0.7303    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"
                + "2.2727    0.7758    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"
                + "M  END";
        Assert.assertFalse(instance.isEmptyMolecule(molecule));
    }
}
