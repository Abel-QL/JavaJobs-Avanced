package zd.zona_fit;


import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import zd.zona_fit.GUI.ZonaFitForm;

import javax.swing.*;

@SpringBootApplication
public class ZonaFitSwing {
    static void main() {
        FlatMacDarkLaf.setup();
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(ZonaFitSwing.class).headless(false).web(WebApplicationType.NONE).run();

        SwingUtilities.invokeLater(() -> {
            ZonaFitForm zf = context.getBean(ZonaFitForm.class);
            zf.setVisible(true);
        });
    }
}
