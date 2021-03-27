package xyz.kings.herbot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static xyz.kyngs.herbot.util.UrlUtil.*;

public class UrlUtilTests {

    @Test
    void testSameUrl() {
        var firstUrl = "https://www.novinky.cz/internet-a-pc/hry-a-herni-systemy/clanek/cesti-vyvojari-her-zazivaji-zlate-casy-40353971#dop_ab_variant=0&dop_source_zone_name=novinky.sznhp.box&dop_req_id=5j4Zdm0xWXU-202103171545&source=hp&seq_no=7&utm_campaign=&utm_medium=z-boxiku&utm_source=www.seznam.cz";
        var secondUrl = "https://www.novinky.cz/internet-a-pc/hry-a-herni-systemy/clanek/cesti-vyvojari-her-zazivaji-zlate-casy-40353971?foo=bar";

        assertEquals(tryNormalizeUrl(firstUrl), tryNormalizeUrl(secondUrl));
    }

    @Test
    void testDifferentUrls() {
        var firstUrl = "https://www.novinky.cz/internet-a-pc/hry-a-herni-systemy/clanek/cesti-vyvojari-her-zazivaji-zlate-casy-40353971#dop_ab_variant=0&dop_source_zone_name=novinky.sznhp.box&dop_req_id=5j4Zdm0xWXU-202103171545&source=hp&seq_no=7&utm_campaign=&utm_medium=z-boxiku&utm_source=www.seznam.cz";
        var secondUrl = "https://www.google.com/";

        assertNotEquals(tryNormalizeUrl(firstUrl), tryNormalizeUrl(secondUrl));
    }

    @Test
    void testHttps() {
        var firstUrl = "http://www.google.com/";
        var secondUrl = "https://www.google.com/";

        assertEquals(tryNormalizeUrl(firstUrl), tryNormalizeUrl(secondUrl));
    }
}
