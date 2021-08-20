package ru.efremov.playgrondproject.adaptersample

import ru.efremov.playgrondproject.R

object DataFactory {

    fun makeGenres(): List<Genre> {
        return listOf(
            makeRockGenre(),
            makeJazzGenre(),
            makeClassicGenre(),
            makeSalsaGenre(),
            makeBluegrassGenre()
        )
    }

    private fun makeRockGenre(): Genre {
        return Genre(
            "Rock",
            makeRockArtists(),
            R.drawable.ic_add_shopping_cart_black_24dp
        )
    }

    private fun makeJazzGenre(): Genre {
        return Genre(
            "Jazz",
            makeJazzArtists(),
            R.drawable.ic_add_shopping_cart_black_24dp
        )
    }

    private fun makeClassicGenre(): Genre {
        return Genre(
            "Classic",
            makeClassicArtists(),
            R.drawable.ic_add_shopping_cart_black_24dp
        )
    }

    private fun makeSalsaGenre(): Genre {
        return Genre(
            "Salsa",
            makeSalsaArtists(),
            R.drawable.ic_add_shopping_cart_black_24dp
        )
    }

    private fun makeBluegrassGenre(): Genre {
        return Genre(
            "Bluegrass",
            makeBluegrassArtists(),
            R.drawable.ic_add_shopping_cart_black_24dp
        )
    }

    private fun makeRockArtists(): List<Artist> {
        val queen = Artist("Queen", true)
        val styx = Artist("Styx", false)
        val reoSpeedwagon = Artist("REO Speedwagon", false)
        val boston = Artist("Boston", true)
        return listOf(queen, styx, reoSpeedwagon, boston)
    }

    private fun makeJazzArtists(): List<Artist> {
        val milesDavis = Artist("Miles Davis", true)
        val ellaFitzgerald = Artist("Ella Fitzgerald", true)
        val billieHoliday = Artist("Billie Holiday", false)
        return listOf(milesDavis, ellaFitzgerald, billieHoliday)
    }

    private fun makeClassicArtists(): List<Artist> {
        val beethoven = Artist("Ludwig van Beethoven", false)
        val bach = Artist("Johann Sebastian Bach", true)
        val brahms = Artist("Johannes Brahms", false)
        val puccini = Artist("Giacomo Puccini", false)
        return listOf(beethoven, bach, brahms, puccini)
    }

    private fun makeSalsaArtists(): List<Artist> {
        val hectorLavoe = Artist("Hector Lavoe", true)
        val celiaCruz = Artist("Celia Cruz", false)
        val willieColon = Artist("Willie Colon", false)
        val marcAnthony = Artist("Marc Anthony", false)
        return listOf(hectorLavoe, celiaCruz, willieColon, marcAnthony)
    }

    private fun makeBluegrassArtists(): List<Artist> {
        val billMonroe = Artist("Bill Monroe", false)
        val earlScruggs = Artist("Earl Scruggs", false)
        val osborneBrothers = Artist("Osborne Brothers", true)
        val johnHartford = Artist("John Hartford", false)
        return listOf(billMonroe, earlScruggs, osborneBrothers, johnHartford)
    }
}