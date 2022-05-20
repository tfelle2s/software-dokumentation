package Test.backend;

import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class GraphImageGenerationUnitTest {
    static DefaultDirectedGraph<String, DefaultEdge> g;

    @Before
    public void createGraph() throws IOException {
        File imgFile = new File("/Users/tobiasfellechner/Downloads/uploads/test.png");
        imgFile.createNewFile();
        g = new DefaultDirectedGraph<>(DefaultEdge.class);
        String x1 = "x1";
        String x2 = "x2";
        String x3 = "x3";
        g.addVertex(x1);
        g.addVertex(x2);
        g.addVertex(x3);
        g.addEdge(x1, x2);
        g.addEdge(x2, x3);
        g.addEdge(x3, x1);
    }
/*
    @After
    public void cleanup() {
        File imgFile = new File("/Users/tobiasfellechner/Downloads/uploads/test.png");
        imgFile.deleteOnExit();
    }


 */
    @Test
    public void givenAdaptedGraph_whenWriteBufferedImage_ThenFileShouldExist() throws IOException {

        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        File imgFile = new File("/Users/tobiasfellechner/Downloads/uploads/test.png");
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 3, Color.WHITE, true, null);
        ImageIO.write(image, "PNG", imgFile);
        assertTrue(imgFile.exists());
    }
}
