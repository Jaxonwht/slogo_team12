package view;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import view.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SidebarView {
    public static final int SIDEBAR_VIEW_WIDTH = 75;

    private static final int BUTTON_MARGIN = 10;
    private static final int BUTTON_SIZE = 50;

    private static final int BACKGROUND_BUTTON = 0;
    private static final int TURTLE_IMAGE_BUTTON = 1;
    private static final int PEN_COLOR_BUTTON = 2;
    private static final int LANGUAGE_BUTTON = 3;
    private static final int HELP_BUTTON = 4;

    private static final List<Image> ICONS = Collections.unmodifiableList(List.of(
            ImageUtils.getImageFromUrl("background_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("turtle_image_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("pen_color_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("language_button.png", BUTTON_SIZE, BUTTON_SIZE),
            ImageUtils.getImageFromUrl("help_button.png", BUTTON_SIZE, BUTTON_SIZE)
    ));

    private static final List<String> TOOLTIPS = Collections.unmodifiableList(List.of(
            "Set background color",
            "Set turtle image",
            "Set pen color",
            "Set language",
            "Open documentation"
    ));

    private Pane root;
    private VBox icons;
    private List<StackPane> buttons;
    private List<Tooltip> tooltips;
    private ColorPicker backgroundColor, penColor;
    private StackPane speedWrapper;
    private Slider speed;

    SidebarView() {
        root = new Pane();
        root.getStyleClass().add("sidebar");

        icons = new VBox(BUTTON_MARGIN);
        icons.getStyleClass().add("sidebar");

        backgroundColor = new ColorPicker();
        backgroundColor.getStyleClass().add("background-button");
        penColor = new ColorPicker();
        penColor.getStyleClass().add("pen-color-button");
        setupButtons();

        speedWrapper = new StackPane();
        speed = new Slider(0.1, 10, 0.1); // TODO: remove magic values
        speed.setOrientation(Orientation.VERTICAL);
        speed.setShowTickMarks(true);
        speed.setShowTickLabels(true);
        setTooltip(speed, "Adjust the duration of single movement");
        speedWrapper.getChildren().add(speed);
        speedWrapper.setAlignment(Pos.CENTER);

        icons.getChildren().addAll(buttons);
        icons.getChildren().add(speedWrapper);
        root.getChildren().add(icons);
    }

    private void setupButtons() {
        buttons = new ArrayList<>();
        tooltips = new ArrayList<>();
        for(int i = 0 ; i < ICONS.size() ; i ++) {
            StackPane button;
            Node image;
            if(i == BACKGROUND_BUTTON || i == PEN_COLOR_BUTTON) {
                image = i == BACKGROUND_BUTTON ? backgroundColor : penColor;
            } else image = new ImageView(ICONS.get(i));
            button = new StackPane(image);
            button.getStyleClass().add("sidebar-box");
            var tooltip = setTooltip(button, TOOLTIPS.get(i));
            buttons.add(button);
            tooltips.add(tooltip);
        }
        appendLanguageTooltip("current: English");
        buttons = Collections.unmodifiableList(buttons);
    }

    private Tooltip setTooltip(Node node, String text) {
        var tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setHideDelay(Duration.ZERO);
        Tooltip.install(node, tooltip);
        return tooltip;
    }

    public void appendLanguageTooltip(String appended) {
        tooltips.get(LANGUAGE_BUTTON).setText(TOOLTIPS.get(LANGUAGE_BUTTON) + "\n" + appended);
    }

    public Pane view() { return root; }

    public ColorPicker backgroundColor() { return backgroundColor; }
    public StackPane turtleImageButton() { return buttons.get(TURTLE_IMAGE_BUTTON); }
    public ColorPicker penColor() { return penColor; }
    public StackPane languageButton() { return buttons.get(LANGUAGE_BUTTON); }
    public StackPane helpButton() { return buttons.get(HELP_BUTTON); }
    public Slider speedSlider() { return speed; }
}
