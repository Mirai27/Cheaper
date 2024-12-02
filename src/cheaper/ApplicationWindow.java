package cheaper;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationWindow {

    public ApplicationWindow() {
        // Установка Look and Feel
        setLookAndFeel();
    }

    private void setLookAndFeel() {
        try {
             // Установка Nimbus Look and Feel
             for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                 if ("Nimbus".equals(info.getName())) {
                     UIManager.setLookAndFeel(info.getClassName());
                     break;
                 }
             }

            // Установка FlatLaf Look and Feel
            UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApplicationWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ApplicationWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ApplicationWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApplicationWindow::new);
    }
}
