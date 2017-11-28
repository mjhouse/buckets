package buckets.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 *
 * @author mhouse
 */
public class CustomTextField extends JTextField {

    private Font originalFont;
    private Color originalForeground;
    private Color placeholderForeground;
    private boolean textWrittenIn;

    /**
     * No arg constructor for the CustomTextField.
     */
    public CustomTextField() {
    }

    /**
     * Set the columns of the TextField.
     *
     * @param columns width to set.
     */
    public CustomTextField(int columns) {
        super(columns);
    }

    /**
     * Set the font of the CustomTextField.
     *
     * @param f font to use.
     */
    @Override
    public void setFont(Font f) {
        super.setFont(f);
        if (!isTextWrittenIn()) {
            originalFont = f;
        }
    }

    /**
     * Set the normal color.
     *
     * @param fg color to use for the normal text.
     */
    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (!isTextWrittenIn()) {
            originalForeground = fg;
        }
    }

    /**
     * Get the placeholder color.
     *
     * @return the color of the placeholder text.
     */
    public Color getPlaceholderForeground() {
        return placeholderForeground;
    }

    /**
     * Set the placeholder color.
     *
     * @param placeholderForeground the color to use for the placeholder text.
     */
    public void setPlaceholderForeground(Color placeholderForeground) {
        this.placeholderForeground = placeholderForeground;
    }

    /**
     * Check if there is non-placeholder text in the CustomTextField.
     *
     * @return whether there is non-placeholder text in the textfield.
     */
    public boolean isTextWrittenIn() {
        return textWrittenIn;
    }

    /**
     * Set a flag for whether text is currently entered.
     *
     * @param textWrittenIn set whether there is non-placeholder text in the
     * field.
     */
    public void setTextWrittenIn(boolean textWrittenIn) {
        this.textWrittenIn = textWrittenIn;
    }

    /**
     * Keep the filler text from overriding entered text.
     */
    public void warn() {
        if (getText().trim().length() != 0) {
            setFont(originalFont);
            setForeground(originalForeground);
            setTextWrittenIn(true);
        }
    }

    /**
     * Set the text to use as a placeholder.
     *
     * @param text text to use as a placeholder.
     */
    public void setPlaceholder(final String text) {
        this.customizeText(text);

        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

        });

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!isTextWrittenIn()) {
                    setText("");
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().trim().length() == 0) {
                    customizeText(text);
                }
            }

        });

    }

    private void customizeText(String text) {
        setText(text);
        setFont(new Font(getFont().getFamily(), Font.PLAIN, getFont().getSize()));
        setForeground(getPlaceholderForeground());
        setTextWrittenIn(false);
    }

}
