import java.text.Normalizer

// https://gist.github.com/adrianoluis/641e21dc24a1dbfb09e203d857ae76a3
fun String.slugify(replacement: String = "-") = Normalizer
    .normalize(this, Normalizer.Form.NFD)
    .replace("[Łł]".toRegex(), "l")
    .replace("[^\\p{ASCII}]".toRegex(), "")
    .replace("[^a-zA-Z0-9\\s]+".toRegex(), "").trim()
    .replace("\\s+".toRegex(), replacement)
    .lowercase()