package ch.epfl.alpano.gui;

/**
 * Some predefined panoramas
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */
public interface PredefinedPanoramas {
    //TODO mettre des magic numbers
    PanoramaUserParameters NIESEN = new PanoramaUserParameters(76_500, 467_300, 600, 180, 110, 300, 2500, 800, 0);
    PanoramaUserParameters JURA_ALPS = new PanoramaUserParameters(68_087, 470_085, 1380, 162, 27, 300, 2500, 800, 0);
    PanoramaUserParameters MOUNT_RACINE = new PanoramaUserParameters(68_200, 470_200, 1500, 135, 45, 300, 2500, 800, 0);
    PanoramaUserParameters FINSTERAARHORN = new PanoramaUserParameters(81_260, 465_374, 4300, 205, 20, 300, 2500, 800, 0);
    PanoramaUserParameters SAUVABELIN_TOUR = new PanoramaUserParameters(66_385, 465_353, 700, 135, 100, 300, 2500, 800, 0);
    PanoramaUserParameters PELICAN_BEACH = new PanoramaUserParameters(65_728, 465_132, 380, 135, 60, 300, 2500, 800, 0);
}
