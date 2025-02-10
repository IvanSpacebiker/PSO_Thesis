import json
from pathlib import Path

import numpy as np
import vtk
import vtkmodules


def visualize_route(route, obstacles):
    points = vtk.vtkPoints()
    for point in route:
        points.InsertNextPoint(*point)

    poly_line = vtk.vtkPolyLine()
    poly_line.GetPointIds().SetNumberOfIds(len(route))
    for i in range(len(route)):
        poly_line.GetPointIds().SetId(i, i)

    cells = vtk.vtkCellArray()
    cells.InsertNextCell(poly_line)

    route_poly_data = vtk.vtkPolyData()
    route_poly_data.SetPoints(points)
    route_poly_data.SetLines(cells)

    mapper = vtk.vtkPolyDataMapper()
    mapper.SetInputData(route_poly_data)

    actor = vtk.vtkActor()
    actor.SetMapper(mapper)
    actor.GetProperty().SetColor(0, 1, 0)

    renderer = vtk.vtkRenderer()
    renderer.AddActor(actor)

    for obstacle in obstacles:
        center, radius = obstacle
        sphere = vtk.vtkSphereSource()
        sphere.SetCenter(*center)
        sphere.SetRadius(radius)

        obstacle_mapper = vtk.vtkPolyDataMapper()
        obstacle_mapper.SetInputConnection(sphere.GetOutputPort())

        obstacle_actor = vtk.vtkActor()
        obstacle_actor.SetMapper(obstacle_mapper)
        obstacle_actor.GetProperty().SetColor(1, 0, 0)

        renderer.AddActor(obstacle_actor)

    renderer.SetBackground(0, 0, 0)
    render_window = vtk.vtkRenderWindow()
    render_window.AddRenderer(renderer)
    render_window.SetSize(800, 600)

    render_window_interactor = vtk.vtkRenderWindowInteractor()
    render_window_interactor.SetInteractorStyle(vtkmodules.vtkInteractionStyle.vtkInteractorStyleTrackballCamera())
    render_window_interactor.SetRenderWindow(render_window)
    render_window_interactor.Initialize()
    render_window.Render()
    render_window_interactor.Start()


def path_to_np_array(data):
    result = []
    for elem in data:
        result.append(np.array([elem['x'], elem['y'], elem['z']]))
    return result


def obstacles_to_np_array(data):
    result = []
    for elem in data:
        result.append((np.array([elem['center']['x'], elem['center']['y'], elem['center']['z']]), elem['radius']))
    return result


if __name__ == "__main__":
    resources_path = Path.cwd().parent.parent.joinpath("resources")
    path = json.load(open(resources_path.joinpath("route.json"), "r"))
    endpoints = json.load(open(resources_path.joinpath("endpoints.json"), "r"))
    obstacles = json.load(open(resources_path.joinpath("obstacles.json"), "r"))

    start = endpoints[0]
    goal = endpoints[1]

    visualize_route(path_to_np_array(path), obstacles_to_np_array(obstacles))
