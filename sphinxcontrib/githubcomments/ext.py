from bp.filepath import FilePath
from docutils import nodes
from docutils.parsers.rst import Directive
from sphinx.application import ExtensionError

from sphinxcontrib.githubcomments import __version__


STATIC_DIR = FilePath(__file__).sibling("_static")


class GitHubCommentsDirective(Directive):
    def run(self):
        document = self.state.document
        config = document.settings.env.config

        repository = config.github_repository
        owner = config.github_repository_owner
        name = config.github_repository_name
        if owner is None or name is None or repository is None:
            raise ExtensionError(
                "The GitHub repository, name and owner must "
                "be set to retrieve comments.",
            )
        return [
            GitHubCommentsNode(
                repository_owner=owner,
                repository_name=name,
                path=FilePath(document.current_source).segmentsFrom(repository)
            ),
        ]


class GitHubCommentsNode(nodes.General, nodes.Element):
    pass


def visit_github_comments_node(self, node):
    html_attrs = {
        "ids" : ["github-comments"],
        "data-repository-owner" : node["repository_owner"],
        "data-repository-name" : node["repository_name"],
        "data-path" : "/".join(seg.encode("utf-8") for seg in node["path"]),
    }
    self.body.append(self.starttag(node, "div", "", **html_attrs))


def depart_github_comments_node(self, node):
    self.body.append(
        '</div>\n'
        '<script async src="_static/githubcomments.js"></script>\n'
        '<link href="_static/githubcomments.css" rel="stylesheet">\n'
    )


def setup(app):
    app.add_config_value(
        name="github_repository",
        default=None,
        rebuild="html",
    )
    app.add_config_value(
        name="github_repository_owner",
        default=None,
        rebuild="html",
    )
    app.add_config_value(
        name="github_repository_name",
        default=None,
        rebuild="html",
    )
    app.add_node(
        GitHubCommentsNode,
        html=(visit_github_comments_node, depart_github_comments_node),
        latex=(lambda _, __ : None, lambda _, __ : None),
    )
    app.add_directive("github-comments", GitHubCommentsDirective)
    app.connect("builder-inited", inject_comment_javascript)

    return dict(version=__version__)


def inject_comment_javascript(app):
    app.config.html_static_path.append(STATIC_DIR.path)
