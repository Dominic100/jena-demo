import matplotlib.pyplot as plt
import networkx as nx
import matplotlib.patches as patches
from matplotlib.patches import FancyBboxPatch
import numpy as np

def create_ps2_games_visualization():
    """Create a network visualization of PS2 games knowledge graph"""
    
    # Create a directed graph
    G = nx.DiGraph()
    
    # Add nodes with attributes
    # Games
    games = [
        ('Final Fantasy X', {'type': 'game', 'rating': 9.0, 'year': 2001}),
        ('GTA: San Andreas', {'type': 'game', 'rating': 9.6, 'year': 2004}),
        ('Tekken 3', {'type': 'game', 'rating': 9.5, 'year': 1998}),
        ('Metal Gear Solid 2', {'type': 'game', 'rating': 9.6, 'year': 2001}),
        ('God of War', {'type': 'game', 'rating': 9.0, 'year': 2005}),
        ('God of War II', {'type': 'game', 'rating': 9.2, 'year': 2007}),
    ]
    
    # Developers
    developers = [
        ('Square Enix', {'type': 'developer', 'country': 'Japan'}),
        ('Rockstar Games', {'type': 'developer', 'country': 'USA'}),
        ('Namco', {'type': 'developer', 'country': 'Japan'}),
        ('Konami', {'type': 'developer', 'country': 'Japan'}),
        ('Santa Monica Studio', {'type': 'developer', 'country': 'USA'}),
    ]
    
    # Characters
    characters = [
        ('Cloud Strife', {'type': 'character', 'role': 'protagonist'}),
        ('Carl Johnson', {'type': 'character', 'role': 'protagonist'}),
        ('Jin Kazama', {'type': 'character', 'role': 'protagonist'}),
        ('Solid Snake', {'type': 'character', 'role': 'protagonist'}),
        ('Kratos', {'type': 'character', 'role': 'protagonist'}),
        ('Sephiroth', {'type': 'character', 'role': 'antagonist'}),
    ]
    
    # Series
    series = [
        ('Final Fantasy Series', {'type': 'series'}),
        ('GTA Series', {'type': 'series'}),
        ('Tekken Series', {'type': 'series'}),
        ('Metal Gear Series', {'type': 'series'}),
    ]
    
    # Genres
    genres = [
        ('RPG', {'type': 'genre'}),
        ('Action', {'type': 'genre'}),
        ('Fighting', {'type': 'genre'}),
    ]
    
    # Add all nodes
    G.add_nodes_from(games)
    G.add_nodes_from(developers)
    G.add_nodes_from(characters)
    G.add_nodes_from(series)
    G.add_nodes_from(genres)
    
    # Add edges (relationships)
    relationships = [
        # Developer-Game relationships
        ('Square Enix', 'Final Fantasy X', {'relation': 'developed'}),
        ('Rockstar Games', 'GTA: San Andreas', {'relation': 'developed'}),
        ('Namco', 'Tekken 3', {'relation': 'developed'}),
        ('Konami', 'Metal Gear Solid 2', {'relation': 'developed'}),
        ('Santa Monica Studio', 'God of War', {'relation': 'developed'}),
        ('Santa Monica Studio', 'God of War II', {'relation': 'developed'}),
        
        # Game-Character relationships
        ('Final Fantasy X', 'Cloud Strife', {'relation': 'hasProtagonist'}),
        ('Final Fantasy X', 'Sephiroth', {'relation': 'hasAntagonist'}),
        ('GTA: San Andreas', 'Carl Johnson', {'relation': 'hasProtagonist'}),
        ('Tekken 3', 'Jin Kazama', {'relation': 'hasProtagonist'}),
        ('Metal Gear Solid 2', 'Solid Snake', {'relation': 'hasProtagonist'}),
        ('God of War', 'Kratos', {'relation': 'hasProtagonist'}),
        ('God of War II', 'Kratos', {'relation': 'hasProtagonist'}),
        
        # Game-Series relationships
        ('Final Fantasy X', 'Final Fantasy Series', {'relation': 'partOfSeries'}),
        ('GTA: San Andreas', 'GTA Series', {'relation': 'partOfSeries'}),
        ('Tekken 3', 'Tekken Series', {'relation': 'partOfSeries'}),
        ('Metal Gear Solid 2', 'Metal Gear Series', {'relation': 'partOfSeries'}),
        
        # Game-Genre relationships
        ('Final Fantasy X', 'RPG', {'relation': 'hasGenre'}),
        ('GTA: San Andreas', 'Action', {'relation': 'hasGenre'}),
        ('Tekken 3', 'Fighting', {'relation': 'hasGenre'}),
        ('Metal Gear Solid 2', 'Action', {'relation': 'hasGenre'}),
        ('God of War', 'Action', {'relation': 'hasGenre'}),
        ('God of War II', 'Action', {'relation': 'hasGenre'}),
        
        # Sequel relationship
        ('God of War II', 'God of War', {'relation': 'isSequelOf'}),
    ]
    
    G.add_edges_from(relationships)
    
    # Create the visualization
    plt.figure(figsize=(16, 12))
    
    # Define colors for different node types
    node_colors = {
        'game': '#FF6B6B',
        'developer': '#4ECDC4',
        'character': '#45B7D1',
        'series': '#96CEB4',
        'genre': '#FFEAA7'
    }
    
    # Create layout
    pos = nx.spring_layout(G, k=3, iterations=50, seed=42)
    
    # Draw nodes by type
    for node_type, color in node_colors.items():
        nodes_of_type = [node for node in G.nodes() if G.nodes[node].get('type') == node_type]
        nx.draw_networkx_nodes(G, pos, nodelist=nodes_of_type, 
                              node_color=color, node_size=1500, 
                              alpha=0.8, edgecolors='black', linewidths=2)
    
    # Draw edges with different styles based on relationship type
    edge_colors = {
        'developed': '#666666',
        'hasProtagonist': '#45B7D1',
        'hasAntagonist': '#FF6B6B',
        'partOfSeries': '#96CEB4',
        'hasGenre': '#FFEAA7',
        'isSequelOf': '#FF9500'
    }
    
    for relation_type, color in edge_colors.items():
        edges_of_type = [(u, v) for u, v, d in G.edges(data=True) if d.get('relation') == relation_type]
        if edges_of_type:
            width = 3 if relation_type == 'isSequelOf' else 2
            nx.draw_networkx_edges(G, pos, edgelist=edges_of_type, 
                                  edge_color=color, width=width, 
                                  alpha=0.7, arrows=True, arrowsize=20)
    
    # Draw labels
    nx.draw_networkx_labels(G, pos, font_size=8, font_weight='bold')
    
    # Add title
    plt.title('PS2 Games Knowledge Graph\nInteractive Visualization of Games, Developers, Characters, and Relationships', 
              fontsize=16, fontweight='bold', pad=20)
    
    # Create legend
    legend_elements = [
        patches.Patch(color=node_colors['game'], label='Games'),
        patches.Patch(color=node_colors['developer'], label='Developers'),
        patches.Patch(color=node_colors['character'], label='Characters'),
        patches.Patch(color=node_colors['series'], label='Series'),
        patches.Patch(color=node_colors['genre'], label='Genres'),
    ]
    
    plt.legend(handles=legend_elements, loc='upper left', bbox_to_anchor=(0, 1))
    
    # Add relationship legend
    relation_legend = [
        patches.Patch(color=edge_colors['developed'], label='Developed by'),
        patches.Patch(color=edge_colors['hasProtagonist'], label='Has Protagonist'),
        patches.Patch(color=edge_colors['hasAntagonist'], label='Has Antagonist'),
        patches.Patch(color=edge_colors['partOfSeries'], label='Part of Series'),
        patches.Patch(color=edge_colors['hasGenre'], label='Has Genre'),
        patches.Patch(color=edge_colors['isSequelOf'], label='Is Sequel Of'),
    ]
    
    plt.legend(handles=relation_legend, loc='upper right', bbox_to_anchor=(1, 1))
    
    plt.axis('off')
    plt.tight_layout()
    
    # Save the visualization
    plt.savefig('ps2_games_network.png', dpi=300, bbox_inches='tight', 
                facecolor='white', edgecolor='none')
    plt.savefig('ps2_games_network.pdf', bbox_inches='tight', 
                facecolor='white', edgecolor='none')
    
    print("Network visualization saved as 'ps2_games_network.png' and 'ps2_games_network.pdf'")
    
    # Show the plot
    plt.show()
    
    # Create a statistics summary
    create_statistics_chart(G)

def create_statistics_chart(G):
    """Create a bar chart showing statistics about the knowledge graph"""
    
    # Count nodes by type
    node_types = {}
    for node in G.nodes():
        node_type = G.nodes[node].get('type', 'unknown')
        node_types[node_type] = node_types.get(node_type, 0) + 1
    
    # Count relationships by type
    relation_types = {}
    for u, v, d in G.edges(data=True):
        relation_type = d.get('relation', 'unknown')
        relation_types[relation_type] = relation_types.get(relation_type, 0) + 1
    
    # Create subplots
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(15, 6))
    
    # Node types chart
    types = list(node_types.keys())
    counts = list(node_types.values())
    colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7']
    
    bars1 = ax1.bar(types, counts, color=colors[:len(types)])
    ax1.set_title('Node Types in PS2 Games Knowledge Graph', fontsize=14, fontweight='bold')
    ax1.set_ylabel('Count')
    
    # Add value labels on bars
    for bar, count in zip(bars1, counts):
        height = bar.get_height()
        ax1.annotate(f'{count}', xy=(bar.get_x() + bar.get_width() / 2, height),
                    xytext=(0, 3), textcoords="offset points",
                    ha='center', va='bottom', fontweight='bold')
    
    # Relationship types chart
    relations = list(relation_types.keys())
    rel_counts = list(relation_types.values())
    
    bars2 = ax2.bar(relations, rel_counts, color='#6C5CE7')
    ax2.set_title('Relationship Types', fontsize=14, fontweight='bold')
    ax2.set_ylabel('Count')
    ax2.tick_params(axis='x', rotation=45)
    
    # Add value labels on bars
    for bar, count in zip(bars2, rel_counts):
        height = bar.get_height()
        ax2.annotate(f'{count}', xy=(bar.get_x() + bar.get_width() / 2, height),
                    xytext=(0, 3), textcoords="offset points",
                    ha='center', va='bottom', fontweight='bold')
    
    plt.tight_layout()
    plt.savefig('ps2_games_statistics.png', dpi=300, bbox_inches='tight')
    plt.savefig('ps2_games_statistics.pdf', bbox_inches='tight')
    
    print("Statistics chart saved as 'ps2_games_statistics.png' and 'ps2_games_statistics.pdf'")
    plt.show()

if __name__ == "__main__":
    create_ps2_games_visualization()
