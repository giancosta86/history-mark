package info.gianlucacosta.historymark.gui.main.world;

import info.gianlucacosta.atlas.openstreetmap.tiles.OpenStreetMapTileFactory;
import info.gianlucacosta.atlas.openstreetmap.tiles.OpenStreetMapTileFactoryInfo;

import java.nio.file.Path;

public class WorldTileFactory extends OpenStreetMapTileFactory {
    public WorldTileFactory(
            OpenStreetMapTileFactoryInfo tileFactoryInfo,
            Path geoCacheRootDirectory
    ) {
        super(
                tileFactoryInfo,

                2,

                geoCacheRootDirectory,

                false
        );
    }
}
