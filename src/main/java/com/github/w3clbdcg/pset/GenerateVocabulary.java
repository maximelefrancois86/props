/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.w3clbdcg.pset;

import com.github.thesmartenergy.sparql.generate.jena.SPARQLGenerate;
import com.github.thesmartenergy.sparql.generate.jena.engine.PlanFactory;
import com.github.thesmartenergy.sparql.generate.jena.engine.RootPlan;
import com.github.thesmartenergy.sparql.generate.jena.locator.LocatorDirectoryAccept;
import com.github.thesmartenergy.sparql.generate.jena.query.SPARQLGenerateQuery;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.stream.StreamManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author maxime.lefrancois
 */
public class GenerateVocabulary {

    private static final Logger LOG = LogManager.getLogger(GenerateVocabulary.class);
    
    private static final String base = "https://w3id.org/product/props/";
    private static final int MAX = Integer.MAX_VALUE;

    /**
     * @param args - arguments. Only the first argument is important: it must be absent, IFC4, or IFC2X3
     **/
    public static void main(String[] args) throws IllegalArgumentException, IOException, URISyntaxException {

        final String focus;
        if(args==null || args.length ==0) {
            focus = "IFC4";
        } else if (args[0].equals("IFC4") || args[0].equals("IFC2X3")) {
            focus = args[0];
        } else {
            throw new IllegalArgumentException("valid argument must be provided: IFC4 or IFC2X3");
        }

        final File resourcesDir = new File(GenerateVocabulary.class.getResource("/").toURI());

        // initialize stream manager
        final StreamManager sm = SPARQLGenerate.getStreamManager();
        sm.addLocator(new LocatorDirectoryAccept(base, resourcesDir));

//         load query
        final String queryLocation = "queries/" + focus + "-transform.rqg";
        LOG.info("Applying query " + queryLocation + " on property sets from " + focus);
        final String queryString = IOUtils.toString(sm.open(queryLocation), "UTF-8");

        final String baseLocation = "ontologies/" + focus + "-base.ttl";
        final Model model = RDFDataMgr.loadModel(baseLocation,Lang.TTL);
        

        final String outputLocation = focus + "-output.ttl";
        try (FileOutputStream output = new FileOutputStream(new File(outputLocation))) {

            // walk in directories that contain string "focus".
            Iterator<File> fileit = FileUtils.iterateFiles(new File(resourcesDir, focus), new String[]{"xml"}, true);

            SPARQLGenerateQuery query = (SPARQLGenerateQuery) QueryFactory.create(queryString, SPARQLGenerate.SYNTAX);

            RootPlan queryPlan = PlanFactory.create(query);
            int i=0;
            while (fileit.hasNext() && i++<MAX) {
                File psetFile = fileit.next();

                String relativePath = resourcesDir.toURI().relativize(psetFile.toURI()).toString();

                sm.getLocationMapper().addAltEntry(base + "replace-with-source-url", base + relativePath);

                model.add(queryPlan.exec());
                
                sm.getLocationMapper().removeAltEntry(base + "replace-with-source-url");
            }
            model.write(output, "TTL");

            LOG.info("Transformation successful, see file at " + outputLocation + " on property sets from " + focus);

        }

    }
}
