package com.oracle.simulator.site.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.simulator.site.exceptions.InputException;
import com.oracle.simulator.site.exceptions.SiteSimulatorException;
import com.oracle.simulator.site.model.SiteMap;

/**
 * This has the functionality to load and return sitemap from a file or from std
 * input
 *
 * @author Joby
 *
 */
public class SiteMapLoader {
    private static Logger LOGGER = LoggerFactory.getLogger(SiteMapLoader.class);

    private File file;

    public SiteMapLoader(File file) {
        this.file = file;
    }

    /**
     * Map is assumed to be in the 4th quadrant of a Cartesian plane. The bulldozer
     * would enter the site from (0,0) and move on the X-axis and can move down
     * along Y-axis
     *
     * @throws SiteSimulatorException
     */
    public SiteMap loadSiteMap() throws SiteSimulatorException {
        SiteMap siteMap = new SiteMap();
        if (file == null) {
            System.out.println("Enter sitemap details in the console. Enter blank line to finish input.");
        }
        InputStream fileStream = null;
        Scanner scanner = null;
        try {
            int numCols = 0;
            int yAxis = 0;
            if (file != null) {
                fileStream = new FileInputStream(file);
                scanner = new Scanner(fileStream);
            } else {
                scanner = new Scanner(System.in);
            }
            String inputLine = null;
            while (!(inputLine = scanner.nextLine().trim()).isEmpty()) {
                if (numCols == 0) {
                    numCols = inputLine.length();
                } else if (inputLine.length() != numCols) {
                    throw new InputException("Disparate number of columns. Cannot proceed.. exiting...");
                }
                for (int i = 0; i < numCols; i++) {
                    Character characteristic = inputLine.charAt(i);
                    siteMap.addEntry(i, yAxis, characteristic);
                }
                yAxis--;
            }
            // yAxis+1 because if there is no next element, we need to adjust the variable
            // yAxis
            siteMap.setBounds(numCols - 1, yAxis + 1);
        } catch (FileNotFoundException e) {
            throw new InputException("Could not find the path of the file " + (file != null ? file.getName() : ""));
        } finally {
            // We should close only File based stream, and not System.in (So this cannot be
            // done using autoclose with a common code)
            if (file != null && scanner != null) {
                scanner.close();
                try {
                    fileStream.close();
                } catch (IOException e) {
                    LOGGER.warn("Error occurred while trying to close file:" + file.getName());
                }
            }
        }
        return siteMap;
    }

}
