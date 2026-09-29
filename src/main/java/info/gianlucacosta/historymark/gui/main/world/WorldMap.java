package info.gianlucacosta.historymark.gui.main.world;

import info.gianlucacosta.atlas.openstreetmap.OpenStreetMapViewer;
import info.gianlucacosta.atlas.pin.OvalPinDrawing;
import info.gianlucacosta.atlas.pin.PinDrawing;
import info.gianlucacosta.atlas.pin.PinLayer;
import info.gianlucacosta.historymark.model.Location;
import info.gianlucacosta.historymark.model.Pin;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public class WorldMap extends OpenStreetMapViewer {
    private static final GeoPosition START_POSITION =
            new GeoPosition(49, 9);

    private static final int START_OPEN_STREET_MAP_ZOOM = 5;

    private final Consumer<Pin> onInteractiveEditPinRequest;
    private final Consumer<Pin> onInteractiveRemovePinRequest;
    private final PinLayer<Pin> pinLayer;


    public WorldMap(
            WorldTileFactory worldTileFactory,
            Consumer<GeoPosition> onMouseGeoPositionChanged,
            Consumer<Pin> onInteractiveAddPinRequest,
            Consumer<Pin> onInteractiveEditPinRequest,
            Consumer<Pin> onInteractiveRemovePinRequest
    ) {
        super(worldTileFactory);

        this.onInteractiveEditPinRequest = onInteractiveEditPinRequest;
        this.onInteractiveRemovePinRequest = onInteractiveRemovePinRequest;


        setAddressLocation(START_POSITION);
        setOpenStreetMapZoom(START_OPEN_STREET_MAP_ZOOM);

        MouseInputListener panMouseInputListener =
                new PanMouseInputListener(this);

        addMouseListener(panMouseInputListener);
        addMouseMotionListener(panMouseInputListener);

        addMouseWheelListener(
                new ZoomMouseWheelListenerCenter(this)
        );

        setFocusable(true);

        addKeyListener(
                new PanKeyListener(this)
        );


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                GeoPosition mouseGeoPosition =
                        convertPointToGeoPosition(e.getPoint());

                onMouseGeoPositionChanged.accept(mouseGeoPosition);
            }
        });


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                requestFocus();

                if (SwingUtilities.isLeftMouseButton(event) &&
                        event.getClickCount() == 2) {
                    GeoPosition mouseGeoPosition =
                            convertPointToGeoPosition(event.getPoint());

                    PinDialog pinDialog =
                            new PinDialog(
                                    new Location(
                                            mouseGeoPosition
                                    )
                            );

                    Optional<Pin> newPinOption =
                            pinDialog.show();

                    newPinOption.ifPresent(onInteractiveAddPinRequest);
                }
            }
        });


        pinLayer =
                new PinLayer<>(
                        this,
                        this::createPinDrawing

                );

        setOverlayPainter(pinLayer);
    }


    private PinDrawing<Pin> createPinDrawing(Pin pin) {
        return new OvalPinDrawing<>(
                pin,
                new Dimension(24, 24),
                this::handlePinDrawingClicked
        );
    }

    private void handlePinDrawingClicked(Pin pin, MouseEvent event) {
        if (event.getClickCount() == 1) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                PinDialog pinDialog =
                        new PinDialog(pin);

                Optional<Pin> editedPinOption =
                        pinDialog.show();

                editedPinOption.ifPresent(onInteractiveEditPinRequest);
            } else if (SwingUtilities.isRightMouseButton(event)) {
                onInteractiveRemovePinRequest.accept(pin);
            }
        }
    }


    public void setPins(Collection<Pin> pins) {
        pinLayer.setPins(pins);
    }

    public void ensurePin(Pin pin) {
        pinLayer.ensurePin(pin);
    }

    public void removePin(Pin pin) {
        pinLayer.removePin(pin);
    }
}
